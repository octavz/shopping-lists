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

        public async Task<ResLoginDTO> GetUser(string token)
        {

            string sResJson = await m_httpHelper.HttpGet(Constants.URL_GET_USER, token);
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

        public async Task<ResLoginDTO> Login(ReqLoginDTO req)
        {

            string sResJson = await m_httpHelper.HttpPost(req, Constants.URL_LOGIN, req.AuthorizationToken);
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
            string sResJson = await m_httpHelper.HttpPost(req, Constants.URL_CREATE_ACCOUNT, req.AuthorizationToken);
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


        public async Task<ResUserLists> GetUserLists(string userId,string token)
        {
            string sUrl = string.Format(Constants.URL_GET_USER_LISTS + "?offset=0&count=1000", userId);
            string sResJson = await m_httpHelper.HttpGet(sUrl, token);
            try
            {
                ResUserLists obj = JsonConvert.DeserializeObject<ResUserLists>(sResJson);
                return obj;
            }
            catch (Exception e)
            {
                return new ResUserLists();
            }
        }//GetUserLists

        public static UserRepository Instance { get { return m_Repository.Value; } }
    }
}