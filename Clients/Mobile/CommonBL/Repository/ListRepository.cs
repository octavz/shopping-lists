using CommonBL.Abstracts;
using CommonBL.Data.Request;
using CommonBL.Data.Response;
using CommonBL.Utils;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Repository
{
    public class ListRepository
    {
         
        private static readonly Lazy<ListRepository> m_Repository = new Lazy<ListRepository>(() => new ListRepository());

        private IHttpHelper m_httpHelper = HelperFactory.GetHttpHelper();

        private ListRepository()
        {
        }

        public async Task<ResListDTO> CreateList(ReqListDTO req)
        {
            string sResJson = await m_httpHelper.HttpPut(req, Constants.URL_CREATE_LIST, req.AuthorizationToken);
            try
            {
                ResListDTO obj = JsonConvert.DeserializeObject<ResListDTO>(sResJson);
                return obj;
            }
            catch (Exception e)
            {
                return new ResListDTO();
            }
        }//CreateList


        public async Task<ResDeleteListDTO> DeleteList(string listId, string sToken)
        {
            string sUrl = string.Format(Constants.URL_DELETE_LIST, listId);
            string sResJson = await m_httpHelper.HttpDelete(sUrl, sToken);
            try
            {
                ResDeleteListDTO obj = JsonConvert.DeserializeObject<ResDeleteListDTO>(sResJson);
                return obj;
            }
            catch (Exception e)
            {
                return new ResDeleteListDTO();
            }
        }//CreateList

        public static ListRepository Instance { get { return m_Repository.Value; } }
    }
}
