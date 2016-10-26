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
        private const string AUTHORIZATION_HEADER = "Bearer";
        private const string MEDIA_TYPE = "application/json";

        public async Task<string> HttpGet(string path, string authToken)
        {
            HttpClient client = SetCustomHttpClient(authToken);

            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, path);            
            HttpResponseMessage msgRes = null;

            using (msgRes = await client.SendAsync(request))
            {
                string cntRes = await msgRes.Content.ReadAsStringAsync();
                return cntRes;
            }
        }//HttpGet

        public Task<string> HttpDelete<T>(T req, string path, string authToken) where T : class
        {
            throw new NotImplementedException();
        }


        public Task<string> HttpPatch<T>(T req, string path, string authToken) where T : class
        {
            throw new NotImplementedException();
        }

        public async Task<string> HttpPut<T>(T req, string path, string authToken) where T : class
        {
            var json = JsonConvert.SerializeObject(req);
            var content = new StringContent(json, Encoding.UTF8, MEDIA_TYPE);

            HttpClient client = SetCustomHttpClient(authToken);

            HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Post, path);
            request.Content = content;

            HttpResponseMessage msgRes = null;

            using (msgRes = await client.SendAsync(request))
            {
                string cntRes = await msgRes.Content.ReadAsStringAsync();
                return cntRes;
            }
        }//HttpPut

        private static HttpClient SetCustomHttpClient(string authToken)
        {
            HttpClient client = new HttpClient();
            client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue(AUTHORIZATION_HEADER, authToken ?? string.Empty);
            client.BaseAddress = new Uri(Constants.SERVER);
            client.DefaultRequestHeaders
                  .Accept
                  .Add(new MediaTypeWithQualityHeaderValue(MEDIA_TYPE));//ACCEPT header
            return client;
        }//SetCustomHttpClient
    }
}
