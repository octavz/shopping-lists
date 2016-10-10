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

namespace ShList.Code.Controls
{
    [Register("CtrlShoppingList")]
    public class CtrlShoppingList: AControl
    {
        private ShoppingListDTO m_Data = null;
        private Activity m_ParentActivity = null;

        public event Action<int> Event_DeleteItem = null;

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
            TextView lstNm = FindViewById<TextView>(Resource.Id.txtListName);
            TextView lstDate = FindViewById<TextView>(Resource.Id.txtDate);
            
            LinearLayout llShopingListMain = FindViewById<LinearLayout>(Resource.Id.llShopingListMain);
            ImageButton btnDelete = FindViewById<ImageButton>(Resource.Id.btnDelete);
            lstNm.Text = m_Data.ListName;
            lstDate.Text = m_Data.ListDate.ToString("MMM/dd/yyy hh:mm:ss");
            llShopingListMain.Click += ShoppingList_Click;
            btnDelete.Click += BtnDelete_Click;
        }

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

            alert.SetPositiveButton(Resource.String.Yes, (senderAlert, args) => {
                if (Event_DeleteItem != null)
                    Event_DeleteItem(this.Id);
            });

            alert.SetNegativeButton(Resource.String.No, (senderAlert, args) => {
                //Toast.MakeText(ShApplicationContext, "Bad", ToastLength.Short).Show();
            });
            alert.Show();
        }//BtnDelete_Click

        private void ShoppingList_Click(object sender, EventArgs e)
        {
            Toast.MakeText(ShApplicationContext, "Click on item", ToastLength.Short).Show();
        }
        

        //public string ListName   { get { return string.IsNullOrEmpty(m_Data.ListName) ? string.Empty : m_Data.ListName; } }
        
    }//CtrlShoppingList
}