using CommonBL.Abstracts;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CommonBL.Data.Request
{
    public class ReqLoginDTO : ARequestDTO
    {
        public ReqLoginDTO(string token) : base(token) { }

        [JsonProperty("login")]
        public string Login { get; set; }
        [JsonProperty("password")]
        public string Password { get; set; }
    }
}