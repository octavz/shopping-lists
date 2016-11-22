using CommonBL.Data.Request;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Data.Response
{
    public class ResSyncDTO: ReqSyncDTO
    {

        [JsonProperty("errCode")]
        public int ErrorCode { get; set; }
        [JsonProperty("message")]
        public string Message { get; set; }
    }
}
