using CommonBL.Abstracts;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Data.Response
{
    public class ResUserLists: AResponseDTO
    {
        [JsonProperty("items")]
        public List<ResListDTO> LstItems { get; set; }

        [JsonProperty("total")]
        public int TotalItems { get; set; }
    }
}
