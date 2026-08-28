from pathlib import Path
from PIL import Image, ImageDraw

source = Path(r"D:\Ltrinh TBDD\StateTestApp\outputs\01a0397b-ae26-7f12-a82e-0c59a49790af\verification_renders")
paths = [source / f"TC{i:02}.png" for i in range(1, 26)]
thumb_w, thumb_h = 360, 220
label_h = 24
canvas = Image.new("RGB", (thumb_w * 5, (thumb_h + label_h) * 5), "white")
draw = ImageDraw.Draw(canvas)

for index, item in enumerate(paths):
    image = Image.open(item).convert("RGB")
    image.thumbnail((thumb_w - 8, thumb_h - 8))
    x = (index % 5) * thumb_w
    y = (index // 5) * (thumb_h + label_h)
    draw.text((x + 6, y + 3), item.stem, fill="black")
    image_x = x + (thumb_w - image.width) // 2
    image_y = y + label_h + (thumb_h - image.height) // 2
    canvas.paste(image, (image_x, image_y))
    draw.rectangle((x, y, x + thumb_w - 1, y + thumb_h + label_h - 1), outline="#94A3B8", width=1)

canvas.save(source / "All_Evidence_Contact_Sheet.png")
