using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class Streets
    {
        public int id { get; set; }
        public string LatitudeA { get; set; }
        public string LongitudeA { get; set; }
        public string LatitudeB { get; set; }
        public string LongitudeB { get; set; }
    }
}