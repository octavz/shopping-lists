using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


using Newtonsoft.Json;
using CommonBL.Abstracts;
using CommonBL.Data.Response;
using CommonBL.Data.Request;
using System.Threading.Tasks;
using CommonBL.Utils;
using System.Net.Http;

namespace CommonBL.Repository
{
    public class UserRepository
    {
        private static readonly Lazy<UserRepository> m_Repository = new Lazy<UserRepository>(() => new UserRepository());

        private IHttpHelper m_httpHelper = HelperFactory.GetHttpHelper();

        private UserRepository()
        {
        }

        public async Task<ResLoginDTO> Login(ReqLoginDTO req)
        {                        
            string sJson = await m_httpHelper.HttpGet(req, "http://xxx.com");
            ResLoginDTO obj = JsonConvert.DeserializeObject<ResLoginDTO>(sJson);
            return obj;
        }


        public async Task<ResNewAccountDTO> CreateAccount(ReqNewAccountDTO req)
        {
            string sResJson = await m_httpHelper.HttpPut(req, Constants.URL_CREATE_ACCOUNT);
            try
            {
                ResNewAccountDTO obj = JsonConvert.DeserializeObject<ResNewAccountDTO>(sResJson);
                return obj;
            }
            catch (Exception e)
            {
                return new ResNewAccountDTO();
            }
        }//CreateAccount

        public static UserRepository Instance { get { return m_Repository.Value; } }
    }
}