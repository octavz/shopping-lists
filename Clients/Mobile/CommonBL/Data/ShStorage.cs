using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Data
{
    public class ShStorage
    {
        public ShStorage()
        {
            ShLists = new List<ShoppingListDTO>();
            SyncJsonHash = string.Empty;
        }

        public List<ShoppingListDTO> ShLists { get; set; }

        public string SyncJsonHash { get; set; }
    }
}
