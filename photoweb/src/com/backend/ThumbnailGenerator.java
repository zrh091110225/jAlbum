package com.backend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThumbnailGenerator
{
    private static final Logger logger = LoggerFactory.getLogger(ThumbnailGenerator.class);

    public static InputStream generateThumbnail(String fpath, int w, int h, boolean force)
    {
        File imgFile = new File(fpath);
        if (imgFile.exists())
        {
            try
            {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG,
                // JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames());
                String suffix = null;
                // 获取图片后缀
                if (imgFile.getName().indexOf(".") > -1)
                {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                } // 类型和图片后缀全部小写，然后判断后缀是否合法
                if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0)
                {
                    logger.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return null;
                }
                logger.debug("target image's size, width:{}, height:{}.", w, h);
                Image img = ImageIO.read(imgFile);
                if (!force)
                {
                    // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if ((width * 1.0) / w < (height * 1.0) / h)
                    {
                        if (width > w)
                        {
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
                            logger.debug("change image's height, width:{}, height:{}.", w, h);
                        }
                    }
                    else
                    {
                        if (height > h)
                        {
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
                            logger.debug("change image's width, width:{}, height:{}.", w, h);
                        }
                    }
                }

                ByteArrayOutputStream bf = new ByteArrayOutputStream();
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                ImageIO.write(bi, suffix, bf);
                byte[] b = bf.toByteArray();
                ByteArrayInputStream bfin = new ByteArrayInputStream(b);
                return bfin;
            }
            catch (IOException e)
            {
                logger.error("generate thumbnail image failed.", e);
            }
        }
        else
        {
            logger.warn("the image is not exist.");
        }

        return null;
    }
}
