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

namespace ShList.Code
{
    [Activity(Label = "@string/ApplicationName", MainLauncher = true, Icon = "@drawable/icon")]
    public class AcMain : AActivity
    {
        Button btnLogin = null;
        EditText txtPassword = null;
        EditText txtEmail = null;
        LinearLayout llLinkSignUp = null;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcMain);

            txtEmail = FindViewById<EditText>(Resource.Id.txtEmail);
            txtPassword = FindViewById<EditText>(Resource.Id.txtPassword);
            llLinkSignUp = FindViewById<LinearLayout>(Resource.Id.llLinkSignUp);

            llLinkSignUp.Click += (s,e) => {
                StartActivity(typeof(AcCreateAccount));
            };
            

            btnLogin = FindViewById<Button>(Resource.Id.btnLogin);
            btnLogin.Click += Btnlogin_Click;
             //Finish();

              //StartActivity(typeof(AcShoppingLists));
        }//OnCreate


        private void Btnlogin_Click(object sender, EventArgs e)
        {
            ReqLoginDTO reqLogin = new ReqLoginDTO()
            {
                 Email =txtEmail.Text,
                 Password =txtPassword.Text
            };

            ResLoginDTO resLogin = new UserRepository().Login(reqLogin);
        }//Btn_login_Click
    }
}

