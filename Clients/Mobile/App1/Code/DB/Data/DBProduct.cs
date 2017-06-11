using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using SQLite;

namespace ShList.Code.DB.Data
{
    [Table("Products")]
    public class DBProduct
    {
        [PrimaryKey]
        public int Id { get; set; }

        public string Name { get; set; }

        public string Tags { get; set; }

        public string Description { get; set; }

        public string ClientTag { get; set; }
    }
}