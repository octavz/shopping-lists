using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using App1.Code.Abstracts;
using App1.Code.Controls;

namespace App1.Code
{
    [Activity(Label = "@string/ApplicationName", MainLauncher = true, Icon = "@drawable/icon")]
    public class AcMain : AActivity
    {       
        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcMain);

            // Get our button from the layout resource,
            // and attach an event to it
            LinearLayout llShoppingLst = FindViewById<LinearLayout>(Resource.Id.listShopping);
            for (int i = 0; i < 40; i++)
            {
                CtrlShoppingList item = new CtrlShoppingList(ShApplicationContext, "My List " + i);
                llShoppingLst.AddView(item);
            }//for
            //button.Click += delegate { button.Text = string.Format("{0} clicks!", count++); };
        }
    }
}

