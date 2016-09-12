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
using ShList.Code.Data;
using ShList.Code.Controls;

namespace ShList.Code
{

    [Activity(Label = "@string/ActivityShoppingList")]
    public class AcShoppingLists : AActivity
    {
        LinearLayout llShoppingLst = null;
        Button btnCreateList = null;        

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcShoppingLists);

            // Get our button from the layout resource,
            // and attach an event to it
            llShoppingLst = FindViewById<LinearLayout>(Resource.Id.listShopping);
            btnCreateList = FindViewById<Button>(Resource.Id.btnCreateList);            

            btnCreateList.Click += AddNewList;
        }//OnCreate

        private void AddNewList(object sender, EventArgs e)
        {
            ShoppingListDTO lstData = new ShoppingListDTO() { ListName = DateTime.Now.ToString() };

            CtrlShoppingList item = new CtrlShoppingList(ShApplicationContext, lstData);
            llShoppingLst.AddView(item);
        }//AddNewList
    }
}
