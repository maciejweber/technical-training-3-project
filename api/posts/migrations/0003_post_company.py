# Generated by Django 4.0.5 on 2022-06-17 13:25

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('posts', '0002_rename_phone_number_post_salary_from_and_more'),
    ]

    operations = [
        migrations.AddField(
            model_name='post',
            name='company',
            field=models.CharField(default=1, max_length=100),
            preserve_default=False,
        ),
    ]