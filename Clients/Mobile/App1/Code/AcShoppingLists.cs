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

    [Activity(Label = "@string/ActivityShoppingList")]
    public class AcShoppingLists : APrivateActivity
    {
        LinearLayout llShoppingLst = null;
        Button btnCreateList = null;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcShoppingLists);
            
            llShoppingLst = FindViewById<LinearLayout>(Resource.Id.listShopping);
            btnCreateList = FindViewById<Button>(Resource.Id.btnCreateList);

            btnCreateList.Click += AddNewList;

        }//OnCreate

        private void AddNewList(object sender, EventArgs e)
        {
            ShoppingListDTO newList = ListsManager.Instance.CreateNewList();

            CtrlShoppingList item = new CtrlShoppingList(this, ShApplicationContext, newList);
            item.Event_DeleteItem += (int id) =>
            {
                var view = FindViewById<CtrlShoppingList>(id);
                ((view as View).Parent as ViewGroup).RemoveView(view);
                ListsManager.Instance.DeleteList(view.Data);
            };
            llShoppingLst.AddView(item, 0);
        }//AddNewList


    }
}
