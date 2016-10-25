using CommonBL.Abstracts;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CommonBL.Data.Response
{
    public class ResLoginDTO: AResponseDTO
    {
        [JsonProperty("login")]
        public string Login { get; set; }
        [JsonProperty("password")]
        public string Password { get; set; }
        [JsonProperty("accessToken")]
        public string AccessToken { get; set; }
        [JsonProperty("nick")]
        public string Nick { get; set; }
        [JsonProperty("id")]
        public string Id { get; set; }
    }
}