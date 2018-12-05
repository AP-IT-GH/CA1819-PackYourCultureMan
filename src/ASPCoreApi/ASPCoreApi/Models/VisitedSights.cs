using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class VisitedSights
    {
        public int id { get; set; }
        public int buildingId { get; set; }
        public bool isChecked { get; set; }
        public int userId { get; set; }
        [ForeignKey("userId")]
        public virtual Users user { get; set; }
    }
}
