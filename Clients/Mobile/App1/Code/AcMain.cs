using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using ShList.Code.Abstracts;
using CommonBL.Data.Request;
using CommonBL.Data.Response;
using CommonBL.Repository;
using V7Toolbar = Android.Support.V7.Widget.Toolbar;
using Android.Support.V7.App;
using Android.Support.V4.Widget;
using Android.Support.Design.Widget;

namespace ShList.Code
{
    [Activity(Label = "@string/ApplicationName", MainLauncher = true, Icon = "@drawable/icon")]
    public class AcMain : AActivity
    {
        Button btnLogin = null;
        EditText txtPassword = null;
        EditText txtEmail = null;
        LinearLayout llLinkSignUp = null;

        DrawerLayout drawerLayout;
        NavigationView navigationView;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcMain);

            /*
            var toolbar = FindViewById<V7Toolbar>(Resource.Id.toolbar);
            SetSupportActionBar(toolbar);
            SupportActionBar.SetDisplayHomeAsUpEnabled(true);
            SupportActionBar.SetDisplayShowTitleEnabled(false);
            SupportActionBar.SetHomeButtonEnabled(true);
            SupportActionBar.SetHomeAsUpIndicator(Resource.Drawable.ic_menu);
            drawerLayout = FindViewById<DrawerLayout>(Resource.Id.drawer_layout);
            navigationView = FindViewById<NavigationView>(Resource.Id.nav_view);
            */
            txtEmail = FindViewById<EditText>(Resource.Id.txtEmail);
            txtPassword = FindViewById<EditText>(Resource.Id.txtPassword);
            llLinkSignUp = FindViewById<LinearLayout>(Resource.Id.llLinkSignUp);

            llLinkSignUp.Click += (s,e) => {
                StartActivity(typeof(AcCreateAccount));
            };
            

            btnLogin = FindViewById<Button>(Resource.Id.btnLogin);
            btnLogin.Click += Btnlogin_Click;
         

         
        }//OnCreate


        private void Btnlogin_Click(object sender, EventArgs e)
        {
            ReqLoginDTO reqLogin = new ReqLoginDTO()
            {
                 Email =txtEmail.Text,
                 Password =txtPassword.Text
            };

            ResLoginDTO resLogin = new UserRepository().Login(reqLogin);

            StartActivity(typeof(AcShoppingLists));
        }//Btn_login_Click
    }
}

