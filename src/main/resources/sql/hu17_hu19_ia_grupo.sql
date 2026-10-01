IF OBJECT_ID('dbo.galeria_ia', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.galeria_ia (
        idRetratoIa BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        prompt_descripcion NVARCHAR(MAX) NOT NULL,
        titulo NVARCHAR(255) NOT NULL,
        url_imagen NVARCHAR(500) NOT NULL,
        fecha_creacion DATETIME2 NOT NULL,
        id_recuerdo BIGINT NOT NULL,

        CONSTRAINT FK_galeria_ia_recuerdo
            FOREIGN KEY (id_recuerdo)
            REFERENCES dbo.recuerdo(id_recuerdo)
    );
END;
GO

IF OBJECT_ID('dbo.grupo', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.grupo (
        idGrupo BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        nombre_grupo NVARCHAR(255) NOT NULL,
        descripcion NVARCHAR(1000) NULL,
        idAdultoMayor BIGINT NOT NULL,

        CONSTRAINT FK_grupo_adulto_mayor
            FOREIGN KEY (idAdultoMayor)
            REFERENCES dbo.adulto_mayor(idAdultoMayor)
    );
END;
GO
