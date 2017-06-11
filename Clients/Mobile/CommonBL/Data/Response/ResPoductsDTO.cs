using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Data.Response
{
    public class ResPoductsDTO
    {
        [JsonProperty("items")]
        public List<ProductDTO> LstProducts { get; set; }

        [JsonProperty("offset")]
        public int Offset { get; set; }

        [JsonProperty("count")]
        public int Count { get; set; }


        [JsonProperty("total")]
        public int Total { get; set; }
    }
}
