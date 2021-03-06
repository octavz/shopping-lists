using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Abstracts
{
    public interface IHttpHelper
    {

        Task<string> HttpGet(string path, string authToken);

        Task<string> HttpPost<T>(T req, string path, string authToken) where T : class;

        Task<string> HttpPut<T>(T req, string path, string authToken) where T : class;

        Task<string> HttpDelete(string path, string authToken);
    }
}