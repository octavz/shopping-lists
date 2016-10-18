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
using Android.Support.V4.Widget;
using Android.Support.V7.Widget;

namespace ShList.Code.Abstracts
{
    public abstract class APrivateActivity:AActivity
    {
        DrawerLayout fullView = null;

        public override void SetContentView(int layoutResID)
        {

            fullView = (DrawerLayout )LayoutInflater.Inflate(Resource.Layout.AcBase, null);
            FrameLayout activityContainer = fullView.FindViewById<FrameLayout>(Resource.Id.activity_content);
            LayoutInflater.Inflate(layoutResID, activityContainer, true);
            base.SetContentView(fullView);

            Android.Support.V7.Widget.Toolbar toolbar = fullView.FindViewById<Android.Support.V7.Widget.Toolbar> (Resource.Id.toolbar);
            SetSupportActionBar(toolbar);
            SetTitle(Resource.String.app_name);
            SupportActionBar.SetHomeAsUpIndicator(Resource.Drawable.edit);
            SupportActionBar.SetDisplayHomeAsUpEnabled(true);
            SupportActionBar.SetHomeButtonEnabled(true);
        }

        public override bool OnOptionsItemSelected(IMenuItem item)
        {            
            if (item.ItemId == Android.Resource.Id.Home)
            {
                fullView.OpenDrawer(Android.Support.V4.View.GravityCompat.Start);
                return true;
            }
            return base.OnOptionsItemSelected(item);
        }

    }
}