using System;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

namespace ASPCoreApi.Migrations
{
    public partial class gamestat : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "dots",
                columns: table => new
                {
                    id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    Latitude = table.Column<string>(nullable: true),
                    Longitude = table.Column<string>(nullable: true),
                    Taken = table.Column<bool>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_dots", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "gameStats",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    lifePoints = table.Column<int>(nullable: false),
                    rifle = table.Column<int>(nullable: false),
                    pushBackGun = table.Column<int>(nullable: false),
                    freezeGun = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_gameStats", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "sights",
                columns: table => new
                {
                    id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    Name = table.Column<string>(nullable: true),
                    shortDescription = table.Column<string>(nullable: true),
                    longDescription = table.Column<string>(nullable: true),
                    Longitude = table.Column<string>(nullable: true),
                    Latitude = table.Column<string>(nullable: true),
                    sightImage = table.Column<string>(nullable: true),
                    Website = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_sights", x => x.id);
                });

            migrationBuilder.CreateTable(
                name: "stats",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    highestScore = table.Column<int>(nullable: false),
                    totalScore = table.Column<int>(nullable: false),
                    totalFailed = table.Column<int>(nullable: false),
                    totalSucces = table.Column<int>(nullable: false),
                    totalLost = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_stats", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "users",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    FirstName = table.Column<string>(nullable: true),
                    LastName = table.Column<string>(nullable: true),
                    Username = table.Column<string>(nullable: true),
                    Email = table.Column<string>(nullable: true),
                    PasswordHash = table.Column<byte[]>(nullable: true),
                    PasswordSalt = table.Column<byte[]>(nullable: true),
                    accessLevel = table.Column<int>(nullable: false),
                    StatsId = table.Column<int>(nullable: false),
                    gameStatsId = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_users", x => x.Id);
                    table.ForeignKey(
                        name: "FK_users_stats_StatsId",
                        column: x => x.StatsId,
                        principalTable: "stats",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_users_gameStats_gameStatsId",
                        column: x => x.gameStatsId,
                        principalTable: "gameStats",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_users_StatsId",
                table: "users",
                column: "StatsId");

            migrationBuilder.CreateIndex(
                name: "IX_users_gameStatsId",
                table: "users",
                column: "gameStatsId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "dots");

            migrationBuilder.DropTable(
                name: "sights");

            migrationBuilder.DropTable(
                name: "users");

            migrationBuilder.DropTable(
                name: "stats");

            migrationBuilder.DropTable(
                name: "gameStats");
        }
    }
}
