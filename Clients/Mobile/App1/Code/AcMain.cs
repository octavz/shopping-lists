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
using Android.Net;

namespace ShList.Code
{
    [Activity(Label = "@string/ApplicationName", MainLauncher = true, Icon = "@drawable/icon")]
    public class AcMain : AActivity
    {
        Button btnLogin = null;
        EditText txtPassword = null;
        EditText txtEmail = null;
        LinearLayout llLinkSignUp = null;
       

        protected async override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            var startServiceIntent = new Intent(ShAppContext, typeof(SyncService));
            StartService(startServiceIntent);                      

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


            if (!string.IsNullOrEmpty(ShAppContext.UserToken))
            {
                ConnectivityManager connectivityManager = (ConnectivityManager)GetSystemService(ConnectivityService);
                if (connectivityManager.ActiveNetworkInfo != null && connectivityManager.ActiveNetworkInfo.IsConnected)
                {
                    var progressDialog = ProgressDialog.Show(this, ShAppContext.GetString(Resource.String.PleaseWait), ShAppContext.GetString(Resource.String.LoggingIn), true);
                    ResLoginDTO resLogin = await UserRepository.Instance.GetUser(ShAppContext.UserToken);
                    progressDialog.Dismiss();
                    if (resLogin.ErrorCode == (int)ErrorCodes.UNAUTHORIZED_LOGIN)
                    {
                        ShAppContext.ClearSettingsForLogout();
                        return;
                    }
                }//if there is connection

                //resLogin.AccessToken = ShAppContext.UserToken;//save token
                //ShAppContext.SetUserLoginSettings(resLogin);
                StartActivity(typeof(AcShoppingLists));
                Finish();
                return;
            }//we have a token
          
        }//OnCreate

        /// <summary>
        /// Btnlogin_Click
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
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

            ReqLoginDTO reqLogin = new ReqLoginDTO(null)
            {
                Login = txtEmail.Text,
                Password = txtPassword.Text
            };

            var progressDialog = ProgressDialog.Show(this, ShAppContext.GetString(Resource.String.PleaseWait), ShAppContext.GetString(Resource.String.LoggingIn), true);

            ResLoginDTO resLogin = await UserRepository.Instance.Login(reqLogin);
            progressDialog.Dismiss();

            if (resLogin.ErrorCode == (int)ErrorCodes.UNAUTHORIZED_LOGIN || resLogin.ErrorCode == (int)ErrorCodes.FAILED_LOGIN)
            {
                txtEmail.ShowError(ShAppContext.GetString(Resource.String.InvalidLoginData), ShAppContext);
                return;
            }//endif

            ShAppContext.SetUserLoginSettings(resLogin);

            StartActivity(typeof(AcShoppingLists));
            Finish();
        }//Btn_login_Click


      
    }

}

