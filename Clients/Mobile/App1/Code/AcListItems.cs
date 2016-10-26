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

namespace ShList.Code
{

    [Activity]
    public class AcListItems : APrivateActivity
    {
        LinearLayout llLst = null;
        Button btnAddItem = null;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);
            SetContentView(Resource.Layout.AcListItems);

            llLst = FindViewById<LinearLayout>(Resource.Id.lstItems);
            btnAddItem = FindViewById<Button>(Resource.Id.btnAddItem);

            btnAddItem.Click += AddNewItem;
        }



        private void AddNewItem(object sender, EventArgs e)
        {
            //ShoppingListDTO newList = ListsManager.Instance.CreateNewList();

            CtrlItemList item = new CtrlItemList(this, ShAppContext, new ItemListDTO());           
            llLst.AddView(item, 0);
        }//AddNewList
    }
}