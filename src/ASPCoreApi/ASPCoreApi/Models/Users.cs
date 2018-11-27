using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class Users
    {
        public int Id { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Username { get; set; }
        public string Email { get; set; }
        public byte[] PasswordHash { get; set; }
        public byte[] PasswordSalt { get; set; }       
        public int accessLevel { get; set; }

        public int StatsId { get; set; }
        [ForeignKey("StatsId")]
        public Statistics Stats { get; set; }
        public int gameStatsId { get; set; }
        [ForeignKey("gameStatsId")]
        public GameStats gameStats { get; set; }
        
    }
}
