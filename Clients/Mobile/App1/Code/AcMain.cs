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
using System.Text.RegularExpressions;
using System.Net.Mail;
using Android.Support.V4.Content;
using CommonBL.Utils;
using ShList.Code.Extended;

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

            llLinkSignUp.Click += (s, e) =>
            {
                StartActivity(typeof(AcCreateAccount));
            };


            btnLogin = FindViewById<Button>(Resource.Id.btnLogin);
            btnLogin.Click += Btnlogin_Click;

        }//OnCreate


        private void Btnlogin_Click(object sender, EventArgs e)
        {
            string email = txtEmail.Text;
            string password = txtPassword.Text;
            bool bIsValid = true;

            if (string.IsNullOrEmpty(email) || !Tools.IsEmailValid(email))
            {                
                txtEmail.ShowError("Enter a valid email address", ShApplicationContext);
                bIsValid = false;
            }
            else            
                txtEmail.HideError();


            if (string.IsNullOrEmpty(password) || password.Length < 4 || password.Length > 10)
            {
                txtPassword.ShowError("Password should be between 4 and 10 alphanumeric characters", ShApplicationContext);
                bIsValid = false;
            }
            else
                txtPassword.HideError();

            if (!bIsValid)
                return;

            ReqLoginDTO reqLogin = new ReqLoginDTO()
            {
                Email = txtEmail.Text,
                Password = txtPassword.Text
            };

            ResLoginDTO resLogin = new UserRepository().Login(reqLogin);

            if (resLogin.Code != 0)
            {
            }
            StartActivity(typeof(AcShoppingLists));
        }//Btn_login_Click


      
    }

}

