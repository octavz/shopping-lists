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
using App1.Code.Data.Response;
using App1.Code.Data.Request;
using App1.Code.Abstracts;

namespace App1.Code.Repository
{
    public class UserRepository
    {
        private IHttpHelper m_httpHelper = HelperFactory.GetHttpHelper();

        public ResLoginDTO Login(ReqLoginDTO req)
        {            
            string sEmail = string.Empty;
            int iUserId = 0;
            string sJson = m_httpHelper.HttpGet(req, "http://xxx.com");            
            return new ResLoginDTO() { Email = sEmail, UserId = iUserId };
        }

    }
}