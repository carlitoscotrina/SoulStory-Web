IF OBJECT_ID('dbo.comentario', 'U') IS NULL
BEGIN

    CREATE TABLE dbo.comentario (
        idComentario BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        texto_comentario NVARCHAR(MAX) NOT NULL,
        idUsuario BIGINT NOT NULL,
        id_recuerdo BIGINT NOT NULL,

        CONSTRAINT FK_comentario_usuario
            FOREIGN KEY (idUsuario)
            REFERENCES dbo.usuario(idUsuario),

        CONSTRAINT FK_comentario_recuerdo
            FOREIGN KEY (id_recuerdo)
            REFERENCES dbo.recuerdo(id_recuerdo)
    );

END;
GO

IF OBJECT_ID('dbo.mensaje', 'U') IS NULL
BEGIN

    CREATE TABLE dbo.mensaje (
        id_mensaje BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        contenido NVARCHAR(MAX) NOT NULL,
        fecha_hora DATETIME2 NOT NULL,
        leido BIT NOT NULL DEFAULT 0,
        tipo_remitente NVARCHAR(50) NOT NULL,
        idAsignacion BIGINT NOT NULL,

        CONSTRAINT FK_mensaje_asignacion
            FOREIGN KEY (idAsignacion)
            REFERENCES dbo.asignacion(idAsignacion)
    );

END;
GO
