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
using CommonBL.Data;
using ShList.Extended.Code;
using ShList.Code.Abstracts;

namespace ShList.Code.Controls
{
    [Register("CtrlItemList")]
    public class CtrlItemList : AControl
    {
        private ItemListDTO m_Data = null;
        private Activity m_ParentActivity = null;
        public event Action<int> Event_DeleteItem = null;
        public event Action<string,bool> Event_BuyItem = null;

        TextView lblItem = null;
        TextView lblQuantity = null;
        CheckBox ckBuy = null;
        ImageButton btnDelete = null;

        /// <summary>
        /// CtrlShoppingList
        /// </summary>
        /// <param name="cnt"></param>
        public CtrlItemList(Activity parent, ShApplication cnt, ItemListDTO data) : base(cnt)
        {
            this.Id = View.GenerateViewId();
            m_ParentActivity = parent;
            m_Data = data;
            Inflate(cnt, Resource.Layout.CtrlItemList, this);
            Initialize();
        }//CtrlShoppingList

        void Initialize()
        {
            ckBuy = FindViewById<CheckBox>(Resource.Id.ckItem);
            lblItem = FindViewById<TextView>(Resource.Id.lblItem);
            lblQuantity = FindViewById<TextView>(Resource.Id.lblQuantity);
            lblItem.Text = m_Data.Description;
            lblQuantity.Text = ShAppContext.GetString(Resource.String.Quantity)+":"  + m_Data.Quantity;
            ckBuy.Checked = m_Data.Bought;
            ckBuy.CheckedChange += (x,y) => {
                Event_BuyItem?.Invoke(m_Data.InternalId, y.IsChecked);
            };

            btnDelete = FindViewById<ImageButton>(Resource.Id.btnDelete);
            btnDelete.Click += (o, e) => {

                AlertDialog.Builder alert = new AlertDialog.Builder(m_ParentActivity);
                alert.SetCancelable(false);
                alert.SetTitle(Resource.String.DeleteListItem);

                alert.SetPositiveButton(Resource.String.Yes, (senderAlert, args) =>
                {
                    Event_DeleteItem?.Invoke(this.Id);
                });

                alert.SetNegativeButton(Resource.String.No, (senderAlert, args) => { });
                alert.Show();                
            };
        }//Initialize



        public void UpdateCtrlData(ItemListDTO aData)
        {
            m_Data = aData;
            lblItem.Text = m_Data.Description;
            lblQuantity.Text = ShAppContext.GetString(Resource.String.Quantity) + ":" + m_Data.Quantity;
            this.Invalidate();
        }

        public ItemListDTO Data { get { return m_Data; } }
    }
}