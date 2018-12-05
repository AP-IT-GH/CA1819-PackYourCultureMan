using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class Statistics
    {
        public int Id { get; set; }
        public int highestScore { get; set; }
        public int totalScore { get; set; }
        public int totalFailed { get; set; }
        public int totalSucces { get; set; }
        public int totalLost { get; set; }
        public int userId { get; set; }
        [ForeignKey("userId")]
        public virtual Users user { get; set; }
        
    }
}
