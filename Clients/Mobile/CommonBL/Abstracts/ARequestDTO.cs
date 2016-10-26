using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace CommonBL.Abstracts
{
    public class ARequestDTO
    {
        private string m_tk = string.Empty;

        public ARequestDTO(string AuthorizationToken)
        {
            m_tk = AuthorizationToken ?? string.Empty;
        }

        [JsonIgnore]
        public string AuthorizationToken { get { return m_tk; } }
    }
}
