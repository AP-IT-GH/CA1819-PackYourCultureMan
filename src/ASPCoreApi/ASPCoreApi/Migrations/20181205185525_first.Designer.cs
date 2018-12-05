﻿// <auto-generated />
using System;
using ASPCoreApi.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

namespace ASPCoreApi.Migrations
{
    [DbContext(typeof(DatabaseContext))]
    [Migration("20181205185525_first")]
    partial class first
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "2.1.4-rtm-31024")
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("ASPCoreApi.Models.Dot", b =>
                {
                    b.Property<int>("id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Latitude");

                    b.Property<string>("Longitude");

                    b.Property<bool>("Taken");

                    b.HasKey("id");

                    b.ToTable("dots");
                });

            modelBuilder.Entity("ASPCoreApi.Models.GameStats", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("freezeGun");

                    b.Property<int>("lifePoints");

                    b.Property<int>("pushBackGun");

                    b.Property<int>("rifle");

                    b.Property<int>("userId");

                    b.HasKey("Id");

                    b.HasIndex("userId")
                        .IsUnique();

                    b.ToTable("gameStats");
                });

            modelBuilder.Entity("ASPCoreApi.Models.Sight", b =>
                {
                    b.Property<int>("id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Latitude");

                    b.Property<string>("Longitude");

                    b.Property<string>("Name");

                    b.Property<string>("Website");

                    b.Property<bool>("isVisible");

                    b.Property<string>("longDescription");

                    b.Property<string>("shortDescription");

                    b.Property<string>("sightImage");

                    b.HasKey("id");

                    b.ToTable("sights");
                });

            modelBuilder.Entity("ASPCoreApi.Models.Statistics", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("highestScore");

                    b.Property<int>("totalFailed");

                    b.Property<int>("totalLost");

                    b.Property<int>("totalScore");

                    b.Property<int>("totalSucces");

                    b.Property<int>("userId");

                    b.HasKey("Id");

                    b.HasIndex("userId")
                        .IsUnique();

                    b.ToTable("stats");
                });

            modelBuilder.Entity("ASPCoreApi.Models.Users", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Email");

                    b.Property<string>("FirstName");

                    b.Property<string>("LastName");

                    b.Property<byte[]>("PasswordHash");

                    b.Property<byte[]>("PasswordSalt");

                    b.Property<string>("Username");

                    b.Property<int>("accessLevel");

                    b.Property<int>("skinId");

                    b.HasKey("Id");

                    b.ToTable("users");
                });

            modelBuilder.Entity("ASPCoreApi.Models.VisitedSights", b =>
                {
                    b.Property<int>("id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<int>("buildingId");

                    b.Property<bool>("isChecked");

                    b.Property<int>("userId");

                    b.HasKey("id");

                    b.HasIndex("userId");

                    b.ToTable("visitedSights");
                });

            modelBuilder.Entity("ASPCoreApi.Models.GameStats", b =>
                {
                    b.HasOne("ASPCoreApi.Models.Users", "user")
                        .WithOne("gameStats")
                        .HasForeignKey("ASPCoreApi.Models.GameStats", "userId")
                        .OnDelete(DeleteBehavior.Cascade);
                });

            modelBuilder.Entity("ASPCoreApi.Models.Statistics", b =>
                {
                    b.HasOne("ASPCoreApi.Models.Users", "user")
                        .WithOne("Stats")
                        .HasForeignKey("ASPCoreApi.Models.Statistics", "userId")
                        .OnDelete(DeleteBehavior.Cascade);
                });

            modelBuilder.Entity("ASPCoreApi.Models.VisitedSights", b =>
                {
                    b.HasOne("ASPCoreApi.Models.Users", "user")
                        .WithMany("visitedSights")
                        .HasForeignKey("userId")
                        .OnDelete(DeleteBehavior.Cascade);
                });
#pragma warning restore 612, 618
        }
    }
}
