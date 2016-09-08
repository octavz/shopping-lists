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

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcMain);

            Finish();

            StartActivity(typeof(AcShoppingLists));
    }//OnCreate

    }
}

