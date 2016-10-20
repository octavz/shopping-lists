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
using CommonBL.Utils;
using ShList.Code.Extended;
using CommonBL.Repository;
using CommonBL.Data.Request;
using CommonBL.Data.Response;

namespace ShList.Code
{
    [Activity]
    public class AcNewAccount : AActivity
    {
        Button btnCreate = null;
        EditText txtPassword1 = null;
        EditText txtPassword2 = null;
        EditText txtEmail = null;

        public AcNewAccount()
        {
        }

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.AcNewAccount);

            txtEmail = FindViewById<EditText>(Resource.Id.txtEmail);
            btnCreate = FindViewById<Button>(Resource.Id.btnCreateAccount);
            txtPassword1 = FindViewById<EditText>(Resource.Id.txtPassword1);
            txtPassword2 = FindViewById<EditText>(Resource.Id.txtPassword2);
            btnCreate.Click += BtnCreate_Click;
        }//OnCreate

        private async void BtnCreate_Click(object sender, EventArgs e)
        {
            string email = txtEmail.Text;
            string password1 = txtPassword1.Text;
            string password2 = txtPassword2.Text;
            bool bIsValid = true;

            if (string.IsNullOrEmpty(email) || !Tools.IsEmailValid(email))
            {
                txtEmail.ShowError(ShApplicationContext.GetString(Resource.String.InvalidEmailFormat), ShApplicationContext);
                bIsValid = false;
            }
            else
                txtEmail.HideError();


            if (string.IsNullOrEmpty(password1) || password1.Length < 4 || password1.Length > 10)
            {
                txtPassword1.ShowError(ShApplicationContext.GetString(Resource.String.InvalidPasswordFormat), ShApplicationContext);
                bIsValid = false;
            }
            else
                txtPassword1.HideError();

            if (password1 != password2)
            {
                txtPassword2.ShowError(ShApplicationContext.GetString(Resource.String.PasswordsMatch), ShApplicationContext);
                bIsValid = false;
            }
            else
                txtPassword2.HideError();

            if (!bIsValid)
                return;

            ReqNewAccountDTO reqDTO = new ReqNewAccountDTO() { login = email, password = password1 };
            ResNewAccountDTO resAccount = await UserRepository.Instance.CreateAccount(reqDTO);

            if (resAccount.errCode == (int)ErrorCodes.CREATE_ACCOUNT_ALREADY_EXITS)
            {
                txtEmail.ShowError(ShApplicationContext.GetString(Resource.String.AccountExists), ShApplicationContext);
                return;
            }
        }//BtnCreate_Click
    }
}