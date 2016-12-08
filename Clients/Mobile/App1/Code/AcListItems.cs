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
using ShList.Code.Controls;
using CommonBL.Data;
using CommonBL.Managers;
using CommonBL.Utils;

namespace ShList.Code
{

    [Activity]
    public class AcListItems : APrivateActivity
    {
        ShoppingListDTO m_data = null;

        LinearLayout llLst = null;
        Button btnAddItem = null;        
        EditText txtShoppingItem = null;
        EditText txtQuantity = null;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);
            SetContentView(Resource.Layout.AcListItems);
            string id = Intent.GetStringExtra(Constants.KEY_ID_LIST);
            if (string.IsNullOrEmpty(id))
                return;
            else
            {
                m_data = ListsManager.Instance.GetListByInternalId(id);
                if (m_data == null)
                    return;
            }//else

            llLst = FindViewById<LinearLayout>(Resource.Id.lstItems);
            txtShoppingItem = FindViewById<EditText>(Resource.Id.txtItemName);
            txtQuantity = FindViewById<EditText>(Resource.Id.txtQuantity);
            btnAddItem = FindViewById<Button>(Resource.Id.btnAddItem);
            btnAddItem.Click += AddNewItem;

            GenerateUIListItems();
        }//OnCreate

        /// <summary>
        /// GenerateUILists
        /// </summary>
        private void GenerateUIListItems()
        {
            llLst.RemoveAllViews();
            for (int i=0;i<m_data.Items.Count;i++)
            {
                CreateUIItem(m_data.Items[i],i);
            }
        }//GenerateUILists


        private void CreateUIItem(ItemListDTO newItem, int position = 0)
        {
            CtrlItemList item = new CtrlItemList(this, ShAppContext, newItem);
            //item.Event_DeleteItem += DeleteList;
            //item.Event_EditItem += EditList;
            //item.Event_ClickItem += ClickList;
            llLst.AddView(item, position);
            llLst.RequestLayout();
        }//CreateUIList


        private void AddNewItem(object sender, EventArgs e)
        {
            int quantity = Convert.ToInt32(txtQuantity.Text);
            ItemListDTO newItemDto = ListsManager.Instance.CreateListItem(m_data.InternalId, txtShoppingItem.Text, quantity);
            CreateUIItem(newItemDto, 0);
        }//AddNewList
    }
}