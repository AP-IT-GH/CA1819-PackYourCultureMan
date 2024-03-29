﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class UserDto
    {
        public int UserId { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Username { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }      
        public int skinId { get; set; }       
        public Statistics Stats { get; set; }       
        public GameStats gameStats { get; set; }
        public ICollection<VisitedSights> visitedSights { get; set; }
        public bool accesLevel { get; set; }

    }
}
