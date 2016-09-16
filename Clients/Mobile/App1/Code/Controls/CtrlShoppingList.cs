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

        /// <summary>
        /// CtrlShoppingList
        /// </summary>
        /// <param name="cnt"></param>
        public CtrlShoppingList(ShApplication cnt, ShoppingListDTO data) : base(cnt)
        {
            m_Data = data;

            Inflate(cnt, Resource.Layout.CtrlShoppingList, this);
            Initialize();
        }//CtrlShoppingList

        void Initialize()
        {            
            TextView lstNm = FindViewById<TextView>(Resource.Id.txtListName);
            LinearLayout llShopingListMain = FindViewById<LinearLayout>(Resource.Id.llShopingListMain);
            lstNm.Text = m_Data.ListName;
            llShopingListMain.Click += ShoppingListClick;
        }

        private void ShoppingListClick(object sender, EventArgs e)
        {
            Toast.MakeText(ShApplicationContext, "Click on item", ToastLength.Short).Show();
        }

        public string ListName   { get { return string.IsNullOrEmpty(m_Data.ListName) ? string.Empty : m_Data.ListName; } }
    }//CtrlShoppingList
}