using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace CommonBL.Data
{
    public class ShoppingListDTO
    {

        public ShoppingListDTO()
        {

        }

        public int Id { get; set; }

        public string ListName { get; set; }

        public DateTime ListDate { get; set; }
        
    }
}