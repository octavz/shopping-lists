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

            if (!string.IsNullOrEmpty(ShAppContext.UserToken))
            {
                StartActivity(typeof(AcShoppingLists));
                Finish();
                return;
            }//we have a token

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcMain);         

            txtEmail = FindViewById<EditText>(Resource.Id.txtEmail);
            txtPassword = FindViewById<EditText>(Resource.Id.txtPassword);
            llLinkSignUp = FindViewById<LinearLayout>(Resource.Id.llLinkSignUp);

            llLinkSignUp.Click += (s, e) =>
            {
                var intAcc = new Intent(this, typeof(AcNewAccount));
                StartActivity(intAcc);
                Finish();
            };


            btnLogin = FindViewById<Button>(Resource.Id.btnLogin);
            btnLogin.Click += Btnlogin_Click;

        }//OnCreate


        private async void Btnlogin_Click(object sender, EventArgs e)
        {
            string email = txtEmail.Text;
            string password = txtPassword.Text;
            bool bIsValid = true;

            if (string.IsNullOrEmpty(email) || !Tools.IsEmailValid(email))
            {                
                txtEmail.ShowError(ShAppContext.GetString(Resource.String.InvalidEmailFormat), ShAppContext);
                bIsValid = false;
            }
            else            
                txtEmail.HideError();


            if (string.IsNullOrEmpty(password) || password.Length < 6 || password.Length > 10)
            {
                txtPassword.ShowError(ShAppContext.GetString(Resource.String.InvalidPasswordFormat), ShAppContext);
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
            
            /*
            ResLoginDTO resLogin = await UserRepository.Instance.Login(reqLogin);

            if (resLogin.Code != 0)
            {
            }
            */
            StartActivity(typeof(AcShoppingLists));
            Finish();
        }//Btn_login_Click


      
    }

}

