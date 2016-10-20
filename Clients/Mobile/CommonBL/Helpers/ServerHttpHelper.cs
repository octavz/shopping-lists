using CommonBL.Abstracts;
using CommonBL.Utils;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using System.Threading;

namespace CommonBL.Helpers
{
    public class ServerHttpHelper : IHttpHelper
    {

        public Task<string> HttpGet<T>(T req, string path) where T : class
        {
            throw new NotImplementedException();
        }//HttpGet

        public Task<string> HttpDelete<T>(T req, string path) where T : class
        {
            throw new NotImplementedException();
        }


        public Task<string> HttpPatch<T>(T req, string path) where T : class
        {
            throw new NotImplementedException();
        }

        public async Task<string> HttpPut<T>(T req, string path) where T : class
        {
            var json = JsonConvert.SerializeObject(req);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri(Constants.SERVER);
            client.DefaultRequestHeaders
                  .Accept
                  .Add(new MediaTypeWithQualityHeaderValue("application/json"));//ACCEPT header

            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Post, path);
            request.Content = content;

            HttpResponseMessage msgRes = null;

            using (msgRes = await client.SendAsync(request))
            {
                string cntRes = await msgRes.Content.ReadAsStringAsync();
                return cntRes;
            }
        }//HttpPut

    }
}
