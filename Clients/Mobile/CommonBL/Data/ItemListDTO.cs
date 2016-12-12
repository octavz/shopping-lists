using CommonBL.Data.Request;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Data
{
    public class ItemListDTO
    {
        public ItemListDTO()
        {
        }

        public ItemListDTO(ItemDTO syncDto)
        {
            Bought = (syncDto.bought == 1);
            Date = DateTime.Now;
            Description = syncDto.description;
            InternalId = Guid.NewGuid().ToString();
            IsDeleted = false;
            ProductId = syncDto.productId;
            Quantity = syncDto.quantity;
        }
        
        public string ProductId { get; set; }

        public string InternalId { get; set; }//Application level only

        public string Description { get; set; }

        public DateTime Date { get; set; }

        public int Quantity { get; set; }

        public bool Bought { get; set; }

        public bool IsDeleted { get; set; }
    }
}
