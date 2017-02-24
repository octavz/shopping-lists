using CommonBL.Abstracts;
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
    public class ProductsRepository
    {

        private IHttpHelper m_httpHelper = HelperFactory.GetHttpHelper();

        public ProductsRepository()
        {
        }

        public async Task<ResPoductsDTO> GetProducts(string token)
        {
            string sUrl = string.Format(Constants.URL_GET_PRODUCTS + "?offset=0&count=1000&q=");
            string sResJson = await m_httpHelper.HttpGet(sUrl, token);
            try
            {
                ResPoductsDTO obj = JsonConvert.DeserializeObject<ResPoductsDTO>(sResJson);
                return obj;
            }
            catch (Exception e)
            {
                return new ResPoductsDTO();
            }
        }

    }//ProductsRepository
}
