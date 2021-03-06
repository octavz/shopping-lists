using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using ShList.Code.Abstracts;
using ShList.Extended.Code;
using CommonBL.Data;
using Android.Graphics.Drawables;
using Android.Support.V4.Content;
using ShList.Code.Extended;
using System.Threading.Tasks;
using CommonBL.Data.Response;
using CommonBL.Utils;

namespace ShList.Code.Controls
{
    [Register("CtrlShoppingList")]
    public class CtrlShoppingList : AControl
    {
        private ShoppingListDTO m_Data = null;
        private Activity m_ParentActivity = null;
        private TextView lstNm = null;

        public event Action<int> Event_DeleteItem = null;
        public event Action<int,string> Event_EditItem = null;
        public event Action<int> Event_ClickItem = null;

        /// <summary>
        /// CtrlShoppingList
        /// </summary>
        /// <param name="cnt"></param>
        public CtrlShoppingList(Activity parent, ShApplication cnt, ShoppingListDTO data) : base(cnt)
        {
            this.Id = View.GenerateViewId();
            m_Data = data;
            m_ParentActivity = parent;
            Inflate(cnt, Resource.Layout.CtrlShoppingList, this);
            Initialize();
        }//CtrlShoppingList

        void Initialize()
        {
            lstNm = FindViewById<TextView>(Resource.Id.txtListName);
            TextView lstDate = FindViewById<TextView>(Resource.Id.txtDate);

            LinearLayout llShopingListMain = FindViewById<LinearLayout>(Resource.Id.llShopingListMain);
            ImageButton btnDelete = FindViewById<ImageButton>(Resource.Id.btnDelete);
            ImageButton btnEdit = FindViewById<ImageButton>(Resource.Id.btnEdit);
            lstNm.Text = string.IsNullOrEmpty(m_Data.ListName)  || m_Data.ListName.Contains(Constants.DEFAULT_LIST_NAME) ? ShAppContext.GetString(Resource.String.NewList) : m_Data.ListName;            
            lstDate.Text = m_Data.ListDate.ToString("MMM/dd/yyy hh:mm:ss");
            llShopingListMain.Click += ShoppingList_Click;
            btnDelete.Click += BtnDelete_Click;
            btnEdit.Click += BtnEdit_Click;
        }

        private void BtnEdit_Click(object sender, EventArgs e)
        {
            LayoutInflater layoutInflater = LayoutInflater.From(m_ParentActivity);
            View promptView = layoutInflater.Inflate(Resource.Layout.dlgeditlist, null);
            AlertDialog.Builder editDlg = new AlertDialog.Builder(m_ParentActivity);
            editDlg.SetView(promptView);
            EditText txtLstName = promptView.FindViewById<EditText>(Resource.Id.txtDlgListName);

            editDlg.SetCancelable(false);
            editDlg.SetPositiveButton(Resource.String.Ok, (senderAlert, args) => { });
            editDlg.SetNegativeButton(Resource.String.Cancel, (senderAlert, args) => { });

            AlertDialog alert = editDlg.Create();
            alert.Show();

            alert.GetButton((int)DialogButtonType.Positive).Click += (senderAlert, args) =>
             {
                 if (!string.IsNullOrEmpty(txtLstName.Text))
                 {                     
                     lstNm.Text = txtLstName.Text;
                     Event_EditItem?.Invoke(this.Id, txtLstName.Text);
                     alert.Dismiss();
                 }
                 else                 
                     txtLstName.ShowError(ShAppContext.GetString(Resource.String.EmptyListName), ShAppContext);
                 
             };
        }//BtnEdit_Click

        /// <summary>
        /// BtnDelete_Click
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void BtnDelete_Click(object sender, EventArgs e)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(m_ParentActivity);
            alert.SetCancelable(false);
            alert.SetTitle(Resource.String.DeleteList);

            alert.SetPositiveButton(Resource.String.Yes, (senderAlert, args) =>
            {
                Event_DeleteItem?.Invoke(this.Id);
            });

            alert.SetNegativeButton(Resource.String.No, (senderAlert, args) => { });
            alert.Show();
        }//BtnDelete_Click

        private void ShoppingList_Click(object sender, EventArgs e)
        {
            Event_ClickItem?.Invoke(this.Id);        
        }

        public void UpdateCtrlData(ShoppingListDTO aData)
        {
            m_Data = aData;
            lstNm.Text = string.IsNullOrEmpty(m_Data.ListName) || m_Data.ListName.Contains(Constants.DEFAULT_LIST_NAME) ? ShAppContext.GetString(Resource.String.NewList) : m_Data.ListName;
            this.Invalidate();
        }

       public ShoppingListDTO Data  { get { return m_Data; } }

    }//CtrlShoppingList
}