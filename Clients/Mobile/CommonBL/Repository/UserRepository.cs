using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


using Newtonsoft.Json;
using CommonBL.Abstracts;
using CommonBL.Data.Response;
using CommonBL.Data.Request;

namespace CommonBL.Repository
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