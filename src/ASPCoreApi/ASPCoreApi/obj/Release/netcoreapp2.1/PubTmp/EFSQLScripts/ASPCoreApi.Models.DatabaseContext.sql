IF OBJECT_ID(N'[__EFMigrationsHistory]') IS NULL
BEGIN
    CREATE TABLE [__EFMigrationsHistory] (
        [MigrationId] nvarchar(150) NOT NULL,
        [ProductVersion] nvarchar(32) NOT NULL,
        CONSTRAINT [PK___EFMigrationsHistory] PRIMARY KEY ([MigrationId])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE TABLE [dots] (
        [id] int NOT NULL IDENTITY,
        [Latitude] nvarchar(max) NULL,
        [Longitude] nvarchar(max) NULL,
        [Taken] bit NOT NULL,
        CONSTRAINT [PK_dots] PRIMARY KEY ([id])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE TABLE [sights] (
        [id] int NOT NULL IDENTITY,
        [Name] nvarchar(max) NULL,
        [shortDescription] nvarchar(max) NULL,
        [longDescription] nvarchar(max) NULL,
        [Longitude] nvarchar(max) NULL,
        [Latitude] nvarchar(max) NULL,
        [sightImage] nvarchar(max) NULL,
        [Website] nvarchar(max) NULL,
        [isVisible] bit NOT NULL,
        CONSTRAINT [PK_sights] PRIMARY KEY ([id])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE TABLE [streets] (
        [id] int NOT NULL IDENTITY,
        [LatitudeA] nvarchar(max) NULL,
        [LongitudeA] nvarchar(max) NULL,
        [LatitudeB] nvarchar(max) NULL,
        [LongitudeB] nvarchar(max) NULL,
        CONSTRAINT [PK_streets] PRIMARY KEY ([id])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE TABLE [users] (
        [Id] int NOT NULL IDENTITY,
        [FirstName] nvarchar(max) NULL,
        [LastName] nvarchar(max) NULL,
        [Username] nvarchar(max) NULL,
        [Email] nvarchar(max) NULL,
        [PasswordHash] varbinary(max) NULL,
        [PasswordSalt] varbinary(max) NULL,
        [accessLevel] int NOT NULL,
        [skinId] int NOT NULL,
        CONSTRAINT [PK_users] PRIMARY KEY ([Id])
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE TABLE [gameStats] (
        [Id] int NOT NULL IDENTITY,
        [lifePoints] int NOT NULL,
        [rifle] int NOT NULL,
        [pushBackGun] int NOT NULL,
        [freezeGun] int NOT NULL,
        [userId] int NOT NULL,
        CONSTRAINT [PK_gameStats] PRIMARY KEY ([Id]),
        CONSTRAINT [FK_gameStats_users_userId] FOREIGN KEY ([userId]) REFERENCES [users] ([Id]) ON DELETE CASCADE
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE TABLE [stats] (
        [Id] int NOT NULL IDENTITY,
        [highestScore] int NOT NULL,
        [totalScore] int NOT NULL,
        [totalFailed] int NOT NULL,
        [totalSucces] int NOT NULL,
        [totalLost] int NOT NULL,
        [userId] int NOT NULL,
        CONSTRAINT [PK_stats] PRIMARY KEY ([Id]),
        CONSTRAINT [FK_stats_users_userId] FOREIGN KEY ([userId]) REFERENCES [users] ([Id]) ON DELETE CASCADE
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE TABLE [visitedSights] (
        [id] int NOT NULL IDENTITY,
        [buildingId] int NOT NULL,
        [isChecked] bit NOT NULL,
        [userId] int NOT NULL,
        CONSTRAINT [PK_visitedSights] PRIMARY KEY ([id]),
        CONSTRAINT [FK_visitedSights_users_userId] FOREIGN KEY ([userId]) REFERENCES [users] ([Id]) ON DELETE CASCADE
    );
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE UNIQUE INDEX [IX_gameStats_userId] ON [gameStats] ([userId]);
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE UNIQUE INDEX [IX_stats_userId] ON [stats] ([userId]);
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    CREATE INDEX [IX_visitedSights_userId] ON [visitedSights] ([userId]);
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181205210038_first')
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20181205210038_first', N'2.1.4-rtm-31024');
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181217160643_coins')
BEGIN
    ALTER TABLE [gameStats] ADD [coins] int NOT NULL DEFAULT 0;
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181217160643_coins')
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20181217160643_coins', N'2.1.4-rtm-31024');
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20181227131421_skinid')
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20181227131421_skinid', N'2.1.4-rtm-31024');
END;

GO

IF NOT EXISTS(SELECT * FROM [__EFMigrationsHistory] WHERE [MigrationId] = N'20190119203826_highscores')
BEGIN
    INSERT INTO [__EFMigrationsHistory] ([MigrationId], [ProductVersion])
    VALUES (N'20190119203826_highscores', N'2.1.4-rtm-31024');
END;

GO

