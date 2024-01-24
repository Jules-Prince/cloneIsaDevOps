import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import * as morgan from 'morgan';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.use(morgan('tiny'));

  await app.listen(9080);
}
bootstrap().then(() => console.log('Service listening 👍: ', 9080));
