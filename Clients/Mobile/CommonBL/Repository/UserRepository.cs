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

            string sResJson = await m_httpHelper.HttpPut(req, Constants.URL_LOGIN);
            try
            {
                ResLoginDTO obj = JsonConvert.DeserializeObject<ResLoginDTO>(sResJson);
                return obj;
            }
            catch (Exception e)
            {
                return new ResLoginDTO();
            }
        }


        public async Task<ResLoginDTO> CreateAccount(ReqNewAccountDTO req)
        {
            string sResJson = await m_httpHelper.HttpPut(req, Constants.URL_CREATE_ACCOUNT);
            try
            {
                ResLoginDTO obj = JsonConvert.DeserializeObject<ResLoginDTO>(sResJson);
                return obj;
            }
            catch (Exception e)
            {
                return new ResLoginDTO();
            }
        }//CreateAccount

        public static UserRepository Instance { get { return m_Repository.Value; } }
    }
}