IF OBJECT_ID(N'dbo.[adulto_,mayor_grupo]', 'U') IS NULL
BEGIN

    CREATE TABLE dbo.[adulto_,mayor_grupo] (
        idAdultoMayorGrupo BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        idAdultoMayor BIGINT NOT NULL,
        idGrupo BIGINT NOT NULL,

        CONSTRAINT FK_adulto_mayor_grupo_adulto
            FOREIGN KEY (idAdultoMayor)
            REFERENCES dbo.adulto_mayor(idAdultoMayor),

        CONSTRAINT FK_adulto_mayor_grupo_grupo
            FOREIGN KEY (idGrupo)
            REFERENCES dbo.grupo(idGrupo)
    );

END;
GO

IF OBJECT_ID('dbo.recuerdo_grupo', 'U') IS NULL
BEGIN

    CREATE TABLE dbo.recuerdo_grupo (
        idRecuerdoGrupo BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        id_recuerdo BIGINT NOT NULL,
        idGrupo BIGINT NOT NULL,

        CONSTRAINT FK_recuerdo_grupo_recuerdo
            FOREIGN KEY (id_recuerdo)
            REFERENCES dbo.recuerdo(id_recuerdo),

        CONSTRAINT FK_recuerdo_grupo_grupo
            FOREIGN KEY (idGrupo)
            REFERENCES dbo.grupo(idGrupo)
    );

END;
GO
