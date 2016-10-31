using CommonBL.Abstracts;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Data.Request
{
    public class ReqListDTO : ARequestDTO
    {
        public ReqListDTO(string token) : base(token) { }

        [JsonProperty("id")]
        public string Id { get; set; }

        [JsonProperty("name")]
        public string Name { get; set; }

        [JsonProperty("description")]
        public string Description { get; set; }

        [JsonProperty("userId")]
        public string UserId { get; set; }

        [JsonProperty("created")]
        public long CreatedDate { get; set; }  
    }
}
