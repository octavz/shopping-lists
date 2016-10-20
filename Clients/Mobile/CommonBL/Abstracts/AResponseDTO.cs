using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Abstracts
{
    public abstract class AResponseDTO
    {
        public int errCode { get; set; }
        public string errMessage { get; set; }
    }
}

