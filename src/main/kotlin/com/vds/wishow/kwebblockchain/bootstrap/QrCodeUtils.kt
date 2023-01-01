package com.vds.wishow.kwebblockchain.bootstrap

import io.github.g0dkar.qrcode.QRCode
import io.github.g0dkar.qrcode.render.Colors

object QrCodeUtils {
    private val START_COLOR = Colors.css("#101820FF")
    private val END_COLOR = Colors.css("#4831D4")
    private const val THEME_COLOR = Colors.WHITE
    private const val topToBottom = true

    fun generateQrCodeFromData(information: String): ByteArray {
        val (startR, startG, startB) = Colors.getRGBA(START_COLOR)
        val (endR, endG, endB) = Colors.getRGBA(END_COLOR)
        val qrCode = QRCode(information)
        val qrCodeData = qrCode.encode()
        val qrCodeSize = qrCode.computeImageSize(rawData = qrCodeData)

        val qrCodeCanvas = qrCode.renderShaded(rawData = qrCodeData) { cellData, cellCanvas ->
            if (cellData.dark) {
                val x = cellData.absoluteX()
                val y = cellData.absoluteY()

                for (currY in 0 until cellCanvas.height) {
                    val topBottomPct = pct(x, y + currY, qrCodeSize, qrCodeSize)
                    val bottomTopPct = 1 - topBottomPct

                    val currColor = Colors.rgba(
                        (startR * bottomTopPct + endR * topBottomPct).toInt(),
                        (startG * bottomTopPct + endG * topBottomPct).toInt(),
                        (startB * bottomTopPct + endB * topBottomPct).toInt()
                    )

                    cellCanvas.drawLine(0, currY, cellCanvas.width, currY, currColor)
                }
            } else {
                cellCanvas.fill(THEME_COLOR)
            }
        }
        return qrCodeCanvas.getBytes()
    }

    private fun pct(x: Int, y: Int, width: Int, height: Int): Double =
        if (topToBottom) {
            x.toDouble() / height.toDouble()
        } else {
            y.toDouble() / width.toDouble()
        }
}