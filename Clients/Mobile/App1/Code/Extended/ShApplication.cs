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
using CommonBL.Data.Response;

namespace ShList.Extended.Code
{
    [Application]
    public class ShApplication : Application
    {
        private const string SAVE_FILE_NAME = "Settings";
        private const string KEY_USER_ID = "_userId";
        private const string KEY_USER_TOKEN = "_userToken";
        private const string KEY_USER_NICK = "_userNick";
        private const string KEY_USER_LOGIN = "_userLogin";

        private string m_UserId = null;
        private string m_UserToken = null;
        private string m_UserNick = null;
        private string m_UserLogin = null;

        public string UserId
        {
            get { return m_UserId; }
            set { m_UserId = value; SaveSettings(); }
        }//UserId

        public string UserToken
        {
            get { return m_UserToken; }
            set { m_UserToken = value; SaveSettings(); }
        }//UserToken

        public string UserNick
        {
            get { return m_UserNick; }
            set { m_UserNick = value; SaveSettings(); }
        }//UserNick

        public string UserLogin
        {
            get { return m_UserLogin; }
            set { m_UserLogin = value; SaveSettings(); }
        }//UserNick

        public ShApplication(IntPtr javaReference, JniHandleOwnership transfer) : base(javaReference, transfer)
        {
        }

        /// <summary>
        /// OnCreate
        /// </summary>
        public override void OnCreate()
        {
            base.OnCreate();
            ISharedPreferences settings = LoadSettings();
            if (settings == null)
                return;
            m_UserId = settings.GetString(KEY_USER_ID, null);
            m_UserToken = settings.GetString(KEY_USER_TOKEN, null);
            m_UserNick = settings.GetString(KEY_USER_NICK, null);
            m_UserLogin = settings.GetString(KEY_USER_LOGIN, null);
        }//OnCreate

        private ISharedPreferences LoadSettings()
        {
            try
            {
                var prefs = Application.Context.GetSharedPreferences(SAVE_FILE_NAME, FileCreationMode.Private);                
                return prefs;
            }
            catch (Exception)
            {
                return null;
            }
        }//LoadSettings

        //SetUserLoginSettings
        public void SetUserLoginSettings(ResLoginDTO lg)
        {
            UserId = lg.Id;
            UserToken = lg.AccessToken;
            UserNick = lg.Nick;
            UserLogin = lg.Login;
        }//SetUserLoginSettings

        public void ClearSettingsForLogout()
        {
            m_UserId = null;
            m_UserToken = null;
            m_UserNick = null;
            m_UserLogin = null;
            SaveSettings();
        }//ClearSettingsForLogout

        /// <summary>
        /// SaveSettings
        /// </summary>
        private void SaveSettings()
        {
            var prefs = Application.Context.GetSharedPreferences(SAVE_FILE_NAME, FileCreationMode.Private);
            var prefEditor = prefs.Edit();
            prefEditor.PutString(KEY_USER_ID, m_UserId);
            prefEditor.PutString(KEY_USER_TOKEN, m_UserToken);
            prefEditor.PutString(KEY_USER_NICK, m_UserNick);
            prefEditor.PutString(KEY_USER_LOGIN, m_UserLogin);
            prefEditor.Commit();
        }//SaveSettings

    }//ShApplication
}