using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using App1.Code.Abstracts;
using App1.Code.Controls;
using App1.Code.Data;

namespace App1.Code
{
    [Activity(Label = "@string/ApplicationName", MainLauncher = true, Icon = "@drawable/icon")]
    public class AcMain : AActivity
    {
        LinearLayout llShoppingLst = null;
        Button btnCreateList = null;
        EditText txtListName = null;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcMain);

            // Get our button from the layout resource,
            // and attach an event to it
            llShoppingLst = FindViewById<LinearLayout>(Resource.Id.listShopping);
            btnCreateList = FindViewById<Button>(Resource.Id.btnCreateList);
            txtListName = FindViewById<EditText>(Resource.Id.editListName);

            btnCreateList.Click += AddNewList;
        }//OnCreate

        private void AddNewList(object sender, EventArgs e)
        {
            ShoppingListDTO lstData = new ShoppingListDTO() { ListName = txtListName.Text };

            CtrlShoppingList item = new CtrlShoppingList(ShApplicationContext, lstData);
            llShoppingLst.AddView(item);
        }//AddNewList

    }
}

