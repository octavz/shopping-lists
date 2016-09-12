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
using ShList.Code.Data.Response;
using ShList.Code.Data.Request;
using Newtonsoft.Json;

namespace ShList.Code.Repository
{
    public class UserRepository
    {
        private IHttpHelper m_httpHelper = HelperFactory.GetHttpHelper();

        public ResLoginDTO Login(ReqLoginDTO req)
        {                        
            string sJson = m_httpHelper.HttpGet(req, "http://xxx.com");
            ResLoginDTO obj = JsonConvert.DeserializeObject<ResLoginDTO>(sJson);
            return obj;
        }

    }
}